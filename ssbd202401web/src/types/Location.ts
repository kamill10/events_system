import { PaginationResponse } from "./Pagination";

export interface Location {
  id: string;
  name: string;
  city: string;
  country: string;
  street: string;
  buildingNumber: string;
  postalCode: string;
}

export interface PaginationLocationResponse
  extends PaginationResponse<Location> {}

export interface CreateLocation {
  name: string;
  city: string;
  country: string;
  street: string;
  buildingNumber: string;
  postalCode: string;
}

export interface LocationsWithNumberOfElements {
  locations: Location[];
  totalElements: number;
}

export interface UpdateLocationDataType {
    name: string;
    city: string;
    country: string;
    street: string;
    buildingNumber: string;
    postalCode: string;
}
