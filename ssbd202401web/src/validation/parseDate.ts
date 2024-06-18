export default function parseDate(date: string, accountTimeZone?: string) {
  const a = new Date(new Date(date).setHours(new Date(date).getHours() + 2));
  if (!accountTimeZone) {
    const { timeZone } = Intl.DateTimeFormat().resolvedOptions();
    return a.toLocaleString("pl-PL", { timeZone: timeZone });
  }
  return a.toLocaleString("pl-PL", { timeZone: accountTimeZone });
}

export function parseDateNoOffset(date: string, accountTimeZone?: string) {
  const a = new Date(date);
  if (!accountTimeZone) {
    const { timeZone } = Intl.DateTimeFormat().resolvedOptions();
    return a.toLocaleString("pl-PL", { timeZone: timeZone });
  }
  return a.toLocaleString("pl-PL", { timeZone: accountTimeZone });
}
