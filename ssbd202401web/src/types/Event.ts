import { Dayjs } from "dayjs";
import { PaginationResponse } from "./Pagination.ts";

export interface Event {
  id: string;
  name: string;
  description: string;
  isNotCanceled: boolean;
  startDate: string;
  endDate: string;
}

export interface CreateEventType {
  name: string;
  description: string;
  startDate?: Dayjs;
  endDate?: Dayjs;
}

export interface CreateEventDTOType {
  name: string;
  description: string;
  startDate: string;
  endDate: string;
}

export interface UpdateEventType extends CreateEventType {}

export interface UpdateEventDTOType extends CreateEventDTOType {}

export interface PaginationPastEventResponse
  extends PaginationResponse<Event> {}

export interface EventWithNumberOfElements {
  events: Event[];
  numberOfElements: number;
}
