import { test, expect } from '@playwright/test';

test('addRole', async ({ page }) => {
  // przygotowanie odpowiedniego stanu aplikacji
  await page.goto('https://team-1.proj-sum.it.p.lodz.pl/');
  await page.getByRole('menuitem', { name: 'Log in' }).getByRole('paragraph').click();
  await page.getByLabel('Username*').click();
  await page.getByLabel('Username*').fill('testAdmin');
  await page.getByLabel('Password*').click();
  await page.getByLabel('Password*').fill('P@ssw0rd');
  await page.getByRole('button', { name: 'Log in' }).click();
  await page.getByRole('menuitem', { name: 'Accounts' }).click();
  await page.getByRole('cell', { name: '08839a96-9432-4ada-a9bd-' }).click();
  await page.getByRole('tab', { name: 'Change profile details' }).click();
  await page.getByRole('row', { name: 'Participant' }).getByRole('button').click();
  await page.getByRole('button', { name: 'Yes' }).click();
  await expect(page.getByRole('row', { name: 'Participant' }).getByRole('button')).toBeHidden();
 
  // wykonanie testu przypadku uzycia
  await page.getByRole('button', { name: 'Add role' }).click();
  await page.getByRole('row', { name: 'Participant' }).getByRole('button').click();
  await page.getByRole('button', { name: 'Yes' }).click();
  await expect(page.getByRole('cell', { name: 'Participant' })).toBeVisible();
});