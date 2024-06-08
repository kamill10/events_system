import { SpeakerType } from "./Speaker.ts";
import { RoomType } from "./Room.ts";
import { Event } from "./Event.ts";

export interface SessionDetailedType {
  id: string;
  name: string;
  description: string;
  startTime: string;
  endTime: string;
  maxSeats: number;
  isActive: boolean;
  speaker: SpeakerType;
  room: RoomType;
  event: Event;
}
