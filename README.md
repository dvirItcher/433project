Got it! You want to:

1. **Restore the old (good) policy and apply it again**  
2. **Keep the bad (current) policy saved as a backup**, not deleted

Here’s exactly how to do that in **Trellix ePO (Policy Catalog)**:

---

### 🔁 Step 1: Clone the Bad Policy (Make a Backup)
So you don’t lose the current bad policy:

1. Go to **Menu > Policy Catalog**.
2. Find the **bad policy**.
3. Click the dropdown arrow or right-click it.
4. Select **Duplicate** (or **Clone**).
5. Name it something like: `PolicyName - Backup` or `PolicyName_BadVersion`.

✅ Now you’ve safely backed up the bad policy for future reference or review.

---

### 🔄 Step 2: Apply the Old (Good) Policy
1. Find the **old policy** in the same **Policy Catalog**.
2. Go to **Menu > System Tree**.
3. Select the group or system where the policy needs to be applied.
4. Click **Assigned Policies** tab.
5. For the product (e.g., ENS Threat Prevention, DLP, etc.), select the **old good policy** from the dropdown.
6. Click **Save**.

✅ The old policy will now be applied again to your target systems.

---

### 🔁 Optional: Rename the Bad Policy Clearly
Just to avoid confusion in the future:
- Go to **Policy Catalog**, find the bad policy (or its backup).
- Rename it to something like:  
  `MyPolicyName - Do Not Use (Bad)`  
  or  
  `MyPolicyName - Old Misconfigured Version`.

---

Let me know which specific product (like ENS, DLP, etc.) you're working with, and I can guide you more precisely if needed.
