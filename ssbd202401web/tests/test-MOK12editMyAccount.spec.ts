import { test, expect } from '@playwright/test';

test('editMyAccount', async ({ page }) => {
  await page.goto('https://team-1.proj-sum.it.p.lodz.pl/');
  await page.getByRole('menuitem', { name: 'Log in' }).getByRole('paragraph').click();
  await page.getByLabel('Username*').click();
  await page.getByLabel('Username*').fill('testParticipant');
  await page.getByLabel('Password*').click();
  await page.getByLabel('Password*').fill('P@ssw0rd');
  await page.getByRole('button', { name: 'Log in' }).click();
  await page.getByRole('button').nth(1).click();
  await page.getByText('Profil').click();
  await page.getByRole('tab', { name: 'Change profile details' }).click();
  await page.getByLabel('First Name*').click();
  await page.getByLabel('First Name*').fill('Michał');
  await page.getByRole('button', { name: 'save changes' }).click();
  await page.getByRole('button', { name: 'Yes' }).click();
  await page.getByRole('tab', { name: 'Profile details', exact: true }).click();
  await expect(page.getByRole('cell', { name: 'Michał' })).toBeVisible();
  await expect(page.getByRole('cell', { name: 'Maciej' })).toBeHidden();
  await page.getByRole('tab', { name: 'Change profile details' }).click();
  await page.getByLabel('First Name*').click();
  await page.getByLabel('First Name*').fill('');
  await page.getByLabel('First Name*').fill('Maciej');
  await page.getByRole('button', { name: 'save changes' }).click();
  await page.getByRole('button', { name: 'Yes' }).click();
  await page.getByRole('tab', { name: 'Profile details', exact: true }).click();
  await expect(page.getByRole('cell', { name: 'Maciej' })).toBeVisible();
});