import { test, expect } from "@playwright/test";

test("test", async ({ page }) => {
  await page.goto("https://team-1.proj-sum.it.p.lodz.pl/");
  await page.getByRole("button").nth(1).click();
  await page
    .getByRole("menuitem", { name: "Log in" })
    .getByRole("paragraph")
    .click();
  await page.getByLabel("Username*").click();
  await page.getByLabel("Username*").fill("testParticipant");
  await page.getByLabel("Username*").press("Tab");
  await page.getByLabel("Password*").fill("P@ssw0rd");
  await page.getByLabel("Password*").press("Enter");
  await page.getByRole("button").nth(1).click();
  await page.getByText("Profile").click();
  await page.getByRole("tab", { name: "Change password" }).click();
  await page.getByLabel("Current password*").click();
  await page.getByLabel("Current password*").fill("P@ssw0rd");
  await page.getByLabel("Current password*").press("Tab");
  await page
    .locator("div")
    .filter({ hasText: /^Current password\*$/ })
    .getByLabel("toggle password visibility")
    .press("Tab");
  await page.getByLabel("New password*", { exact: true }).fill("P@ssw0rd1");
  await page.getByLabel("Confirm new password*").click();
  await page.getByLabel("Confirm new password*").fill("P@ssw0rd1");
  await page.getByRole("button", { name: "Save changes" }).click();
  await page.getByRole("button", { name: "Yes" }).click();

  await expect(
    page.getByText("Email with confirmation has been sent!"),
  ).toBeVisible();
});
