import { Location } from "./Location";

export interface RoomType {
  id: string;
  name: string;
  maxCapacity: number;
  location: Location;
}
