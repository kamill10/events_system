import moment from "moment-timezone";

export default function parseDate(date: string) {
  const { timeZone } = Intl.DateTimeFormat().resolvedOptions();
  return moment.utc(date).tz(timeZone).format("YYYY-MM-DD, HH:mm:ss");
}
