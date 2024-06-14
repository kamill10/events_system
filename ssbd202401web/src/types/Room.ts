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

export interface UpdateRoomType {
  name: string;
  maxCapacity: number;
}

export interface GetRoomResponse {
  id: string;
  name: string;
  maxCapacity: number;
}

export interface CreateRoom {
  name: string;
  locationId: string;
  maxCapacity: number;
}

export interface CreateRoomInput {
  name: string;
  maxCapacity: number;
}
