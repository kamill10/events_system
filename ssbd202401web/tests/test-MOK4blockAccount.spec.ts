import { test, expect } from "@playwright/test";

test("blockAccount", async ({ page }) => {
  // wykonanie testu przypadku uzycia
  await page.goto("https://team-1.proj-sum.it.p.lodz.pl/");
  await page
    .getByRole("menuitem", { name: "Log in" })
    .getByRole("paragraph")
    .click();
  await page.getByLabel("Username*").click();
  await page.getByLabel("Username*").fill("testAdmin");
  await page.getByLabel("Username*").press("Tab");
  await page.getByLabel("Password*").fill("P@ssw0rd");
  await page.getByRole("button", { name: "Log in" }).click();
  await page.getByRole("menuitem", { name: "Accounts" }).click();
  await page.getByRole("cell", { name: "08839a96-9432-4ada-a9bd-" }).click();
  await page.getByRole("tab", { name: "Change profile details" }).click();
  await page.getByRole("checkbox").click();
  await page.getByRole("button", { name: "Yes" }).click();
  await expect(
    page.getByText("Account deactivated successfully!"),
  ).toBeVisible();
  await page.getByRole("tab", { name: "Profile details", exact: true }).click();
  await expect(
    page.getByRole("cell", { name: "No", exact: true }),
  ).toBeVisible();

  // powrot do oryginalnego stanu
  await page.getByRole("tab", { name: "Change profile details" }).click();
  await page.getByRole("checkbox").click();
  await page.getByRole("button", { name: "Yes" }).click();
  await expect(page.getByText("Account activated successfully!")).toBeVisible();
  await page.getByRole("tab", { name: "Profile details", exact: true }).click();
  await expect(
    page.getByRole("cell", { name: "Yes", exact: true }).first(),
  ).toBeVisible();
});
