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
  await page.getByRole("button", { name: "Log in" }).click();
  await page.getByRole("button").nth(1).click();
  await page.getByText("Profile").click();
  await page.getByRole("tab", { name: "Change personal data" }).click();
  await page.getByLabel("Male").click();
  await page.getByRole("option", { name: "Female" }).click();
  await page.getByRole("button", { name: "Save changes" }).click();
  await page.getByRole("button", { name: "Yes" }).click();
  await page.getByRole("tab", { name: "Profile details" }).click();

  await expect(page.getByRole("cell", { name: "Female" })).toContainText(
    "Female",
  );
});
