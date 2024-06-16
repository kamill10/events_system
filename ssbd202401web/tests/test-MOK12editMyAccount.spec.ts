import { test, expect } from '@playwright/test';

test('test', async ({ page }) => {
    await page.goto('http://localhost:5173/');
    await page.getByRole('menuitem', { name: 'Log in' }).getByRole('paragraph').click();
    await page.getByLabel('Username*').click();
    await page.getByLabel('Username*').click({
        button: 'right'
    });
    await page.getByLabel('Username*').fill('halinaposwiatoswka12');
    await page.getByLabel('Password*').click();
    await page.getByLabel('Password*').fill('P@ssw0rd');
    await page.getByLabel('Password*').press('Enter');
    await page.getByRole('button').nth(1).click();
    await page.locator('li.MuiButtonBase-root:nth-child(2)').click();
    await page.locator("button.MuiTab-root:nth-child(2)").click();


    await page.locator('#firstName').click();
    await page.locator('#firstName').fill('zmiana');
    await page.locator('#lastName').click();
    await page.locator('#lastName').fill('nowe');
    await page.locator('button.MuiButton-root:nth-child(1)').click();
    await page.locator('button.MuiButton-text:nth-child(1)').click();
    await page.locator('button.MuiTab-root:nth-child(1)').click();
    const cell_name = await page.getByRole('cell', { name: 'zmiana' });
    const cell_surname = await page.getByRole('cell', { name: 'nowe' });
    await expect(cell_name).toBeVisible();
    await expect(cell_surname).toBeVisible();

    //kolejna zmiana
    await page.locator('button.MuiTab-root:nth-child(1)').click();
    await expect(page.getByRole("cell", { name: "Michal" })).toBeHidden();
    await expect(page.getByRole("cell", { name: "Karbowanczyk" })).toBeHidden();
    await page.locator('button.MuiTab-root:nth-child(2)').click();
    await page.locator('#firstName').click();
    await page.locator('#firstName').fill('Michal');
    await page.locator('#lastName').click();
    await page.locator('#lastName').fill('Karbowanczyk');
    await page.locator('button.MuiButton-root:nth-child(1)').click();
    await page.locator('button.MuiButton-text:nth-child(1)').click();
    await page.locator('button.MuiTab-root:nth-child(1)').click();
    const cell_name2 = await page.getByRole('cell', { name: 'Michal' });
    const cell_surname2 = await page.getByRole('cell', { name: 'Karbowanczyk' });
    await expect(cell_name2).toBeVisible();
    await expect(cell_surname2).toBeVisible();
});