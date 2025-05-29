#include <linux/module.h>
#include <linux/kernel.h>
#include <linux/init.h>
#include <linux/proc_fs.h>
#include <linux/seq_file.h>

#define PROC_NAME "uptime"

MODULE_LICENSE("GPL");
MODULE_AUTHOR("ChatGPT");
MODULE_DESCRIPTION("Fake /proc/uptime");
MODULE_VERSION("1.0");

static struct proc_dir_entry *proc_entry;

// This is our custom read function for /proc/uptime
static int fake_uptime_show(struct seq_file *m, void *v)
{
    // Return 10 seconds uptime, 0 seconds idle
    seq_printf(m, "10.00 0.00\n");
    return 0;
}

// Wrappers required by procfs
static int fake_uptime_open(struct inode *inode, struct file *file)
{
    return single_open(file, fake_uptime_show, NULL);
}

static const struct proc_ops fake_uptime_fops = {
    .proc_open = fake_uptime_open,
    .proc_read = seq_read,
    .proc_lseek = seq_lseek,
    .proc_release = single_release,
};

static int __init fake_uptime_init(void)
{
    // Remove the original /proc/uptime (hidden, not deleted)
    remove_proc_entry(PROC_NAME, NULL);

    // Create our fake one
    proc_entry = proc_create(PROC_NAME, 0, NULL, &fake_uptime_fops);
    if (!proc_entry) {
        pr_err("Failed to create /proc/uptime\n");
        return -ENOMEM;
    }

    pr_info("[+] Fake uptime module loaded\n");
    return 0;
}

static void __exit fake_uptime_exit(void)
{
    // Remove our custom /proc/uptime
    remove_proc_entry(PROC_NAME, NULL);
    pr_info("[-] Fake uptime module unloaded\n");
}

module_init(fake_uptime_init);
module_exit(fake_uptime_exit);




obj-m += fake_uptime.o

all:
	make -C /lib/modules/$(shell uname -r)/build M=$(PWD) modules

clean:
	make -C /lib/modules/$(shell uname -r)/build M=$(PWD) clean
