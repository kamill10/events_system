import { AccountTypeEnum} from "./AccountType.ts";

export interface AccountType {
    id: string;
    username: string;
    email: string;
    roles: Array<AccountTypeEnum>;
    gender: number;
    firstName: string;
    lastName: string;
}

export interface AccountLoginType {
    username: string;
    password: string;
}