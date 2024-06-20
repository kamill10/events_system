import { test, expect } from '@playwright/test';

test('logOut', async ({ page }) => {
    await page.goto('https://team-1.proj-sum.it.p.lodz.pl/');
    await page.getByRole('menuitem', { name: 'Log in' }).getByRole('paragraph').click();
    await page.getByLabel('Username*').click();
    await page.getByLabel('Username*').fill('testAdmin');
    await page.getByLabel('Username*').press('Tab');
    await page.getByLabel('Password*').fill('P@ssw0rd');
    await page.getByRole('button', { name: 'Log in' }).click();
    await page.getByRole('button').nth(1).click();
    await page.getByText('Log-out').click();
    await expect(page.getByRole('menuitem', { name: 'Log in' }).getByRole('paragraph')).toBeVisible();
});