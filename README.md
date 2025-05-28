#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/mman.h>
#include <sys/ioctl.h>
#include <linux/fs.h>

#define JIFFIES_ADDR 0xffffffff81c7d8a0  // Example address of 'jiffies' on your system (may vary!)

void write_jiffies(unsigned long new_value) {
    int mem_fd;
    unsigned long *jiffies;

    // Open /dev/mem for raw memory access
    mem_fd = open("/dev/mem", O_RDWR | O_SYNC);
    if (mem_fd == -1) {
        perror("Error opening /dev/mem");
        exit(1);
    }

    // Memory map the physical memory location to the user space
    jiffies = (unsigned long *)mmap(NULL, sizeof(unsigned long), PROT_READ | PROT_WRITE, MAP_SHARED, mem_fd, JIFFIES_ADDR);

    if (jiffies == MAP_FAILED) {
        perror("Memory mapping failed");
        close(mem_fd);
        exit(1);
    }

    // Overwrite the jiffies value
    *jiffies = new_value;

    printf("Successfully modified jiffies value to %lu\n", new_value);

    // Clean up
    munmap(jiffies, sizeof(unsigned long));
    close(mem_fd);
}

int main() {
    // Fake uptime value (set to 0 seconds)
    unsigned long fake_uptime = 0;

    // Call the function to write the fake uptime
    write_jiffies(fake_uptime);

    // Check if it worked by printing the modified /proc/uptime
    system("cat /proc/uptime");

    return 0;
}
