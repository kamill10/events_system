import { test, expect } from "@playwright/test";

test("Admin auth test", async ({ page }) => {
  await page.goto('https://team-1.proj-sum.it.p.lodz.pl/');
  await page.getByRole('menuitem', { name: 'Log in' }).getByRole('paragraph').click();
  await page.getByLabel('Username*').click();
  await page.getByLabel('Username*').fill('testAdmin');
  await page.getByLabel('Password*').click();
  await page.getByLabel('Password*').fill('P@ssw0rd');
  await page.getByRole('button', { name: 'Log in' }).click();

  await expect(page.getByText("Home")).toBeVisible();
});
