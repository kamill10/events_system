import { AccountTypeEnum} from "./AccountType.ts";

export interface Account {
    id: string;
    username: string;
    email: string;
    roles: Array<AccountTypeEnum>;
}

export interface AccountLogin {
    login: string;
    password: string;
}