import { test, expect } from '@playwright/test';

test('changeMyEmail', async ({ browser }) => {
  const context = await browser.newContext();
  const page1 = await context.newPage();

  await page1.goto('https://team-1.proj-sum.it.p.lodz.pl/');
  await page1.getByRole('button').nth(1).click();
  await page1.getByRole('menuitem', { name: 'Log in' }).getByRole('paragraph').click();
  await page1.getByLabel('Username*').click();
  await page1.getByLabel('Username*').fill('testManager');
  await page1.getByLabel('Password*').click();
  await page1.getByLabel('Password*').fill('P@ssw0rd');
  await page1.getByRole('button', { name: 'Log in' }).click();
  await page1.getByRole('button').nth(1).click();
  await page1.getByText('Profile').click();
  await page1.getByRole('tab', { name: 'Change e-mail' }).click();
  await page1.getByLabel('Current password*').click();
  await page1.getByLabel('Current password*').fill('P@ssw0rd');
  await page1.getByLabel('New e-mail*').click();
  await page1.getByLabel('New e-mail*').fill('isrpgrupa1@proton.me');
  await page1.getByRole('button', { name: 'save changes' }).click();
  await page1.getByRole('button', { name: 'Yes' }).click();
  await expect(page1.getByText("Email sent! Confirm your email change")).toBeVisible();
  
  await context.close();
});
