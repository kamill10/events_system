import { PaginationResponse } from "./Pagination.ts";

export interface RoomType {
  id: string;
  name: string;
  maxCapacity: number;
}

export interface PaginationRoomResponse extends PaginationResponse<RoomType> {}

export interface RoomsWithNumberOfElements {
  rooms: RoomType[];
  numberOfElements: number;
}
