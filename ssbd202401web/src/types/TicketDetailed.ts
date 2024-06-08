import { SessionDetailedType } from "./SessionDetailed.ts";

export interface TicketDetailedType {
  id: string;
  session: SessionDetailedType;
  reservationTime: string;
  isNotCancelled: boolean;
}
