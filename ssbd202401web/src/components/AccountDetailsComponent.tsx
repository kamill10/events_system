import { Box } from "@mui/material";
import { GetDetailedAccountType } from "../types/Account";
import ElementComponent from "./ElementComponent";
import parseDate from "../validation/parseDate";

export default function AccountDetailsComponent({ account }: { account: GetDetailedAccountType | null }) {
  return (
    <Box component={Box} margin={"3rem"}>
      <ElementComponent
        label="ID"
        value={account?.id}
      />
      <ElementComponent
        label="Username"
        value={account?.username}
      />
      <ElementComponent
        label="Roles"
        value={JSON.stringify(account?.roles)}
      />
      <ElementComponent
        label="E-mail"
        value={account?.email}
      />
      <ElementComponent
        label="First name"
        value={account?.firstName}
      />
      <ElementComponent
        label="Last name"
        value={account?.lastName}
      />
      <ElementComponent
        label="Gender"
        value={account?.gender}
      />
      <ElementComponent
        label="Is active"
        value={account?.active ? "Yes" : "No"}
        color={account?.active ? "green" : "red"}
      />
      <ElementComponent
        label="Is verified"
        value={account?.verified ? "Yes" : "No"}
        color={account?.verified ? "green" : "red"}
      />
      <ElementComponent
        label="Is unlocked"
        value={account?.nonLocked ? "Yes" : "No"}
        color={account?.nonLocked ? "green" : "red"}
      />
      <ElementComponent
        label="Language preference"
        value={account?.language}
      />
      <ElementComponent
        label="Last successful login"
        value={account?.lastSuccessfulLogin ? parseDate(account?.lastSuccessfulLogin) : "Never"}
      />
      <ElementComponent
        label="Last failed login"
        value={account?.lastFailedLogin ? parseDate(account?.lastFailedLogin) : "Never"}
        color={account?.lastFailedLogin ? "red" : "green"}
      />
      <ElementComponent
        label="Locked until"
        value={account?.lockedUntil ? parseDate(account?.lockedUntil) : "Not locked"}
        color={account?.lockedUntil ? "red" : "green"}
      />
    </Box>
  )
}