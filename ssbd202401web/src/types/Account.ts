import { AccountTypeEnum } from "./enums/AccountType.enum.ts";
import { LanguageType } from "./enums/LanguageType.enum.ts";

export interface GetAccountType {
  id: string;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  roles: AccountTypeEnum[];
  active: boolean;
  verified: boolean;
  nonLocked: boolean;
}

export interface GetPersonalAccountType extends GetAccountType {
  firstName: string;
  lastName: string;
  language: LanguageType;
  gender: number;
  accountTimeZone: string;
  accountTheme: string;
}

export interface GetDetailedAccountType extends GetPersonalAccountType {
  lastSuccessfulLogin: string;
  lastFailedLogin: string;
  lockedUntil: string;
}

export interface UpdatePersonalDataType {
  firstName: string;
  lastName: string;
  gender: number;
  accountTimeZone: string;
}

export interface ChangeMyPasswordType {
  oldPassword: string;
  newPassword: string;
  confirmNewPassword: string;
}

export interface ChangeMyEmailType {
  password: string;
  newEmail: string;
}

export interface ManageAccountType {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  gender: number;
}

export interface ChangeEmailType {
  email: string;
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

export interface PaginationResponse {
  content: GetAccountType[];
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

export interface AccountsWithNumberOfElements {
  accounts: GetAccountType[];
  totalElements: number;
}
