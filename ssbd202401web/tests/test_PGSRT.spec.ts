import { test, expect } from "@playwright/test";

test("sorting_by_name_surname-test", async ({ page }) => {
  await page.goto("http://localhost:5173/");
  await page
    .getByRole("menuitem", { name: "Log in" })
    .getByRole("paragraph")
    .click();
  await page.getByLabel("Username").click();
  await page.getByLabel("Username").fill("testAdmin");
  await page.getByLabel("Password*", { exact: true }).click();
  await page.getByLabel("Password*", { exact: true }).fill("P@ssw0rd");
  await page.getByRole("button", { name: "Log in" }).click();
  await page
    .locator("div.MuiBox-root:nth-child(2) > li:nth-child(1) > p:nth-child(1)")
    .click();
  await page
    .locator("xpath=/html/body/div[1]/div[2]/div/div/div[1]/div[1]/div[1]/h5")
    .click();
  await page.locator("#phrase").fill("Morawski");
  await page.locator("button.MuiButtonBase-root:nth-child(11)").click();
  const containsTestString = await page.waitForSelector(
    '//td[contains(.,"Morawski")]',
  );
  expect(containsTestString).not.toBeNull();
  await page
    .locator("xpath=/html/body/div[1]/div[2]/div/div/div[1]/div[1]/div[1]/h5")
    .click();
  await page.locator("#phrase").fill("Majer");
  await page.locator("button.MuiButtonBase-root:nth-child(11)").click();
  const containsParticipant = await page.waitForSelector(
    '//td[contains(.,"Majer")]',
  );
  expect(containsParticipant).not.toBeNull();
  await expect(page.getByRole("cell", { name: "Morawski" })).toBeHidden();
});
