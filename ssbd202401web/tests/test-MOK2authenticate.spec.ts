import { test, expect } from "@playwright/test";

test("test", async ({ page }) => {
  await page.goto("https://team-1.proj-sum.it.p.lodz.pl/");
  await page.getByRole("button").nth(1).click();
  await page
    .getByRole("menuitem", { name: "Log in" })
    .getByRole("paragraph")
    .click();
  await page.getByLabel("Username*").click();
  await page.getByLabel("Username*").fill("testAdmin");
  await page.getByLabel("Username*").press("Tab");
  await page.getByLabel("Password*").fill("P@ssw0rd");
  await page.getByLabel("Password*").press("Enter");
  await page.getByRole("button").nth(1).click();

  await expect(
    page.getByRole("heading", { name: "Welcome, testAdmin" }),
  ).toBeVisible();
});
