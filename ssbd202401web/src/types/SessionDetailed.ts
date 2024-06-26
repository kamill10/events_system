import { Speaker } from "./Speaker.ts";
import { RoomType } from "./Room.ts";
import { Event } from "./Event.ts";

export interface SessionDetailedType {
  id: string;
  name: string;
  description: string;
  startTime: string;
  endTime: string;
  maxSeats: number;
  availableSeats: number;
  isActive: boolean;
  speaker: Speaker;
  room: RoomType;
  event: Event;
}
