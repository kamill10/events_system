import { Dayjs } from "dayjs";

export interface CreateSessionType {
  eventId: string;
  roomId: string;
  speakerId: string;
  name: string;
  description: string;
  startDate: Dayjs;
  endDate: Dayjs;
  maxSeats: number;
  locationId: string;
}

export interface CreateSessionDTOType {
  eventId: string;
  roomId: string;
  speakerId: string;
  locationId: string;
  name: string;
  description: string;
  startDate: string;
  endDate: string;
  maxSeats: number;
}

export interface UpdateSessionType extends CreateSessionType {}

export interface UpdateSessionDTOType extends CreateSessionDTOType {}
