import { test, expect } from '@playwright/test';

test('viewAccounts', async ({ page }) => {
    await page.goto('https://team-1.proj-sum.it.p.lodz.pl/');
    await page.getByRole('menuitem', { name: 'Log in' }).getByRole('paragraph').click();
    await page.getByLabel('Username*').click();
    await page.getByLabel('Username*').fill('testAdmin');
    await page.getByLabel('Password*').click();
    await page.getByLabel('Password*').fill('P@ssw0rd');
    await page.getByRole('button', { name: 'Log in' }).click();
    await page.getByRole('menuitem', { name: 'Accounts' }).getByRole('paragraph').click();
    await expect(page.getByRole('cell', { name: '08839a96-9432-4ada-a9bd-' })).toBeVisible();
});