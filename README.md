Yes, you **can view lock status from the ePO console**, but with some limitations. Here's how to do it for **policy locks** and **client task locks** using **queries** inside the ePO GUI:

---

## **1. View Policy Locks from the Console**

### Steps:
1. Go to **Menu > Reporting > Queries & Reports**
2. Click **"New Query"**
3. **Query Type**: Select **"Managed Systems"**
4. Click **"Next"**
5. In **Columns**, add:
   - **Computer Name** (or System Name)
   - **Product Family**
   - **Policy Enforcement Status** (This may reflect lock status for some products)
6. Click **"Next"**
7. Add a **filter** like:
   - `Policy Enforcement Status = Locked` (if available)
8. Click **Next**, name the query, and save it.

> **Note**: Not all policy lock details are exposed in the UI for all products, especially for custom policies or 3rd-party extensions. For full visibility, SQL queries offer more detail.

---

## **2. View Client Task Locks from the Console**

### Steps:
1. Go to **Menu > Client Task Catalog**
2. Click on a **task category** (e.g., McAfee Agent, ENS, etc.)
3. You'll see a list of tasks. The **lock icon** next to each task name indicates whether the task is locked.
   - **Closed padlock** = Locked
   - **Open padlock** = Unlocked

4. To view **assigned systems**:
   - Click on the task name
   - Choose **"Assigned Systems"** tab

There’s **no built-in query** for showing all systems with client task lock status — but again, the **SQL query I gave earlier** can help extract that info in bulk.

---

## Want a GUI report instead?

If you prefer a **GUI-based report with lock icons**, we can:
- Build a custom query to reflect lock status as a column
- Or export a list from the **System Tree** with applied columns

Let me know if you want help building a saved custom query or dashboard widget.
