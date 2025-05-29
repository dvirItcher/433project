Absolutely. Here's what the updated version of your script might look like — **with careful clarification** about what parts you **must change**, and what **will and won’t work**.

---

## ⚠️ IMPORTANT REMINDER

> `jiffies` lives in **kernel virtual memory**. Modern Linux kernels **do not allow direct access** to `/dev/mem` in this way anymore (especially after kernel 5.6), unless:
>
> * You **disable KASLR**,
> * You **know the exact physical address**,
> * You boot with `iomem=relaxed`,
> * Or you use a **kernel module**.

If you're okay with this, here's how the script **should** look like — assuming you somehow know the **physical address** of `jiffies`.

---

## ✅ Updated Script (Unsafe But Demonstrative)

```c
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/mman.h>
#include <errno.h>
#include <string.h>

// ⚠️ Replace this with the actual physical address of `jiffies`
#define JIFFIES_PHYS_ADDR 0x1c7d8a0  // Example only – you must get the real physical address!

void write_jiffies(unsigned long new_value) {
    int mem_fd;
    volatile unsigned long *jiffies;

    // Open /dev/mem with read/write access
    mem_fd = open("/dev/mem", O_RDWR | O_SYNC);
    if (mem_fd == -1) {
        perror("Error opening /dev/mem");
        exit(EXIT_FAILURE);
    }

    // Memory map the jiffies location
    jiffies = (unsigned long *)mmap(NULL, sizeof(unsigned long), PROT_READ | PROT_WRITE,
                                    MAP_SHARED, mem_fd, JIFFIES_PHYS_ADDR);

    if (jiffies == MAP_FAILED) {
        fprintf(stderr, "mmap failed: %s\n", strerror(errno));
        close(mem_fd);
        exit(EXIT_FAILURE);
    }

    printf("[+] Current jiffies value: %lu\n", *jiffies);

    // Overwrite the jiffies value
    *jiffies = new_value;

    printf("[+] Successfully modified jiffies to: %lu\n", new_value);

    // Clean up
    munmap((void *)jiffies, sizeof(unsigned long));
    close(mem_fd);
}

int main() {
    unsigned long fake_uptime_ticks = 0; // 0 ticks since boot = zero uptime

    printf("[*] Attempting to write fake jiffies value...\n");
    write_jiffies(fake_uptime_ticks);

    printf("[*] Checking uptime from /proc/uptime:\n");
    system("cat /proc/uptime");

    return 0;
}
```

---

## 🔧 You Must Do These Before Running

1. **Disable KASLR**:

   * Add to GRUB config:

     ```
     GRUB_CMDLINE_LINUX="nokaslr iomem=relaxed"
     ```
   * Then:

     ```bash
     sudo update-grub
     sudo reboot
     ```

2. **Find Physical Address of `jiffies`**:

   * `/proc/kallsyms` gives **virtual address**:

     ```bash
     grep jiffies /proc/kallsyms
     ```
   * You must map this to **physical memory**, which may require a symbol-to-phys mapping tool or manually reverse engineering it.

3. **Run as root**:

   ```bash
   sudo ./fake_jiffies
   ```

---

## ❌ If This Still Fails...

Modern kernels are designed to **block this exact kind of access**.

So if you get:

* `mmap failed: Operation not permitted`, or
* `/dev/mem` doesn’t allow the range...

Then this script won’t work **unless you compile and load a kernel module**.

---

## ✅ Optional Safer Alternative: Kernel Module?

Would you like me to generate a very small **loadable kernel module** that sets `jiffies = 0` (or any number)? It will:

* Work even on modern kernels,
* Not need `/dev/mem` or `mmap()`,
* Be short and safe to compile and unload afterward.

Let me know — I can give you the `.c` file, `Makefile`, and full instructions.
