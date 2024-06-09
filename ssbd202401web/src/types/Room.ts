import { Location } from "./Location.ts";
import { PaginationResponse } from "./Pagination.ts";

export interface RoomType {
  id: string;
  name: string;
  maxCapacity: number;
  location: Location;
}

export interface PaginationRoomResponse extends PaginationResponse<RoomType> {}

export interface RoomsWithNumberOfElements {
  rooms: RoomType[];
  numberOfElements: number;
}
