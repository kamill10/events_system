import { test, expect } from "@playwright/test";

test("resetPassword", async ({ page }) => {
  await page.goto("https://team-1.proj-sum.it.p.lodz.pl/");
  await page
    .getByRole("menuitem", { name: "Log in" })
    .getByRole("paragraph")
    .click();
  await page.getByRole("link", { name: "I forgot my password :(" }).click();
  await page.getByLabel("E-mail").click();
  await page.getByLabel("E-mail").fill("testMail123@email.com");
  await page.getByRole("button", { name: "Change password" }).click();
  await expect(page.getByText("Request sent successfully!")).toBeVisible();
});
