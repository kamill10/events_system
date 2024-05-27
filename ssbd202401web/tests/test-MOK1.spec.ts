import { test, expect } from "@playwright/test";

test("Admin auth test", async ({ page }) => {
  await page.goto("http://localhost:5173");
  await page.getByRole("button").click();
  await page
    .getByRole("menuitem", { name: "Log in" })
    .getByRole("paragraph")
    .click();
  await page.getByLabel("Username").click();
  await page.getByLabel("Username").fill("testAdmin");
  await page.getByLabel("Username").press("Tab");
  await page.getByLabel("Password", { exact: true }).fill("P@ssw0rd");
  await page.getByRole("button", { name: "Log in" }).click();

  await expect(page.getByText("Home")).toBeVisible();
});
