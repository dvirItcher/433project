If you're using **Trellix ePO** and want to create a report or query to **see all "locks" in the database** (e.g., policy locks, client task locks, etc.), here's how you can do that:

---

### **What Do You Mean by "Lock"?**

ePO uses the concept of **"locks"** to prevent local changes to settings on endpoints, such as:

- **Policy locks**: Prevent users from changing policy settings.
- **Client task locks**: Prevent users from disabling or modifying tasks.
- **Product property locks**: For specific product features (e.g., Endpoint Security modules).

These "locks" are stored in the SQL DB as part of the **policy assignments** or in **table fields** indicating lock status.

---

### **Method 1: SQL Query (Run from SQL Server Management Studio)**

```sql
SELECT 
    LN.NodeName AS 'Computer Name',
    P.ProductCode,
    PA.PolicyLockStatus,
    CASE 
        WHEN PA.PolicyLockStatus = 1 THEN 'Locked'
        WHEN PA.PolicyLockStatus = 0 THEN 'Unlocked'
        ELSE 'Unknown'
    END AS 'Lock Status',
    CPG.Name AS 'Policy Name'
FROM 
    EPOLeafNode LN
JOIN 
    EPOPolicyAssignment PA ON LN.AutoID = PA.AgentGUID
JOIN 
    EPOProdPropsView P ON LN.AutoID = P.AgentGUID
JOIN 
    EPOPolicyObject PO ON PA.PolicyID = PO.PolicyID
JOIN 
    EPOPolicyCategory CPG ON PO.CategoryID = CPG.CategoryID
WHERE 
    PA.PolicyLockStatus IS NOT NULL
ORDER BY 
    LN.NodeName;
```

> This query gives you the list of computers, the product, the lock status, and the policy name.

---

### **Method 2: ePO Console – Custom Query**

1. Go to **Menu > Reporting > Queries & Reports**
2. Click **New Query**
3. Choose:
   - **Feature**: "Managed Systems" or relevant product
   - **Display results as**: Table
4. Add Columns like:
   - System Name
   - Product Name
   - Lock status (if available — some products expose lock fields)
5. Use filters to show only systems with locked policies
6. Save and run the query

---

### Let Me Know:
Do you want to see:
- **Policy locks**?
- **Client task locks**?
- Locks for a **specific product** (e.g., ENS, DLP)?

I can tailor the query or report to exactly what you're after.
