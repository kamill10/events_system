import moment from "moment-timezone";

export default function parseDate(date: string, accountTimeZone?: string) {
  if (!accountTimeZone) {
    const { timeZone } = Intl.DateTimeFormat().resolvedOptions();
    return moment.utc(date).tz(timeZone).format("YYYY-MM-DD HH:mm:ss");
  }
  return moment.utc(date).tz(accountTimeZone).format("YYYY-MM-DD HH:mm:ss");
}
