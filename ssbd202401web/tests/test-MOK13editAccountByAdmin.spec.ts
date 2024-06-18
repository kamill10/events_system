import { test, expect } from "@playwright/test";

test("test", async ({ page }) => {
  await page.goto("http://localhost:5173/");
  await page
    .getByRole("menuitem", { name: "Log in" })
    .getByRole("paragraph")
    .click();
  await page.getByLabel("Username*").click();
  await page.getByLabel("Username*").fill("testAdmin");
  await page.getByLabel("Password*").click();
  await page.getByLabel("Password*").fill("P@ssw0rd");
  await page.getByLabel("Password*").press("Enter");
  await page.locator(".css-1l0r181 > li:nth-child(2) > p:nth-child(1)").click();
  await page.locator("tr", { hasText: "halinaposwiatoswka12" }).click();
  await page.locator("button.MuiTab-root:nth-child(2)").click();
  await page.locator("#firstName").click();
  await page.locator("#firstName").fill("zmiana");
  await page.locator("#lastName").click();
  await page.locator("#lastName").fill("nowe");
  await page.locator("button.MuiButtonBase-root:nth-child(10)").click();
  await page.locator(".MuiDialogActions-root > button:nth-child(1)").click();
  await page.locator("button.MuiTab-root:nth-child(1)").click();
  const cell_name = await page.getByRole("cell", { name: "zmiana" });
  const cell_surname = await page.getByRole("cell", { name: "nowe" });
  await expect(cell_name).toBeVisible();
  await expect(cell_surname).toBeVisible();

  //kolejna zmiana
  await expect(page.getByRole("cell", { name: "Jacek" })).toBeHidden();
  await expect(page.getByRole("cell", { name: "Bazant" })).toBeHidden();
  await page.locator(".css-1l0r181 > li:nth-child(2) > p:nth-child(1)").click();
  await page.locator("tr", { hasText: "halinaposwiatoswka12" }).click();
  await page.locator("button.MuiTab-root:nth-child(2)").click();
  await page.locator("#firstName").click();
  await page.locator("#firstName").fill("Jacek");
  await page.locator("#lastName").click();
  await page.locator("#lastName").fill("Bazant");
  await page.locator("button.MuiButtonBase-root:nth-child(10)").click();
  await page.locator(".MuiDialogActions-root > button:nth-child(1)").click();
  await page.locator("button.MuiTab-root:nth-child(1)").click();
  const cell_name2 = await page.getByRole("cell", { name: "Jacek" });
  const cell_surname2 = await page.getByRole("cell", { name: "Bazant" });
  await expect(cell_name2).toBeVisible();
  await expect(cell_surname2).toBeVisible();
});
