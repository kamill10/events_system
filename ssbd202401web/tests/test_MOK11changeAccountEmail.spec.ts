import { test, expect } from '@playwright/test';

test('changeAccountEmail', async ({ page }) => {

  await page.goto('https://team-1.proj-sum.it.p.lodz.pl/');
  await page.getByRole('menuitem', { name: 'Log in' }).getByRole('paragraph').click();
  await page.getByLabel('Username*').click();
  await page.getByLabel('Username*').fill('testAdmin');
  await page.getByLabel('Password*').click();
  await page.getByLabel('Password*').fill('P@ssw0rd');
  await page.getByRole('button', { name: 'Log in' }).click();
  await page.getByRole("menuitem", { name: "Accounts" }).click();
  await page.getByRole('cell', { name: '08839a96-9432-4ada-a9bd-' }).click();
  await page.getByRole('tab', { name: 'Change profile details' }).click();
  await page.getByLabel('New e-mail*').click();
  await page.getByLabel('New e-mail*').fill('nowymail123@proton.me');
  await page.locator('form').filter({ hasText: 'Change e-mail' }).getByRole('button').click();
  await page.getByRole('button', { name: 'Yes' }).click();
  await page.getByText('E-mail changed successfully!').click();

});
