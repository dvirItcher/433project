#include <linux/module.h>
#include <linux/kernel.h>
#include <linux/init.h>
#include <linux/jiffies.h>

MODULE_LICENSE("GPL");
MODULE_AUTHOR("ChatGPT");
MODULE_DESCRIPTION("Kernel module to overwrite jiffies");
MODULE_VERSION("1.0");

static unsigned long new_jiffies = 0;

module_param(new_jiffies, ulong, 0);
MODULE_PARM_DESC(new_jiffies, "New value for jiffies");

static int __init jiffies_patch_init(void)
{
    printk(KERN_INFO "[+] jiffies_patch loaded\n");
    printk(KERN_INFO "[+] Original jiffies: %lu\n", jiffies);

    jiffies = new_jiffies;

    printk(KERN_INFO "[+] Modified jiffies: %lu\n", jiffies);
    return 0;
}

static void __exit jiffies_patch_exit(void)
{
    printk(KERN_INFO "[-] jiffies_patch unloaded\n");
}

module_init(jiffies_patch_init);
module_exit(jiffies_patch_exit);
