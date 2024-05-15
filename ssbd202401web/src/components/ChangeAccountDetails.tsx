import { GetPersonalAccountType } from "../types/Account";
import ChangeAccountDataComponent from "./ChangeAccountDataComponent";
import ChangeAccountPasswordComponent from "./ChangeAccountPasswordComponent";
import ChangeAccountEmailComponent from "./ChangeAccountEmailComponents";

export default function ChangeAccountDetails({ account, fetchAccount }: { account: GetPersonalAccountType | null, fetchAccount: () => void }) {
  return (
    <>
      <ChangeAccountDataComponent account={account} fetchAccount={fetchAccount}></ChangeAccountDataComponent>
      <ChangeAccountPasswordComponent account={account} fetchAccount={fetchAccount}></ChangeAccountPasswordComponent>
      <ChangeAccountEmailComponent account={account} fetchAccount={fetchAccount}></ChangeAccountEmailComponent>
    </>
  )
}