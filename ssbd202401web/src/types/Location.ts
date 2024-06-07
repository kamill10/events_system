export interface Location {
  id: string;
  name: string;
  city: string;
  country: string;
  street: string;
  buildingNumber: string;
  postalCode: string;
}

export interface CreateLocation {
    name: string;
    city: string;
    country: string;
    street: string;
    buildingNumber: string;
    postalCode: string;
}

export interface Sort {
  empty: boolean;
  sorted: boolean;
  unsorted: boolean;
}

export interface Pageable {
  pageNumber: number;
  pageSize: number;
  sort: Sort;
  offset: number;
  paged: boolean;
  unpaged: boolean;
}

export interface PaginationLocationResponse {
  content: Location[];
  pageable: Pageable;
  last: boolean;
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  sort: Sort;
  first: boolean;
  numberOfElements: number;
  empty: boolean;
}

export interface LocationsWithNumberOfElements {
  locations: Location[];
  totalElements: number;
}
