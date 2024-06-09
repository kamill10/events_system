import { PaginationResponse } from "./Pagination";

export interface Ticket {
  id: string;
  name: string;
  startTime: string;
  endTime: string;
  roomName: string;
  locationName: string;
  isNotCancelled: boolean;
}

export interface PaginationTicketResponse extends PaginationResponse<Ticket> {}

export interface TicketWithNumberOfElements {
  tickets: Ticket[];
  numberOfElements: number;
}
