import { yupResolver } from "@hookform/resolvers/yup";
import { useForm, SubmitHandler, SubmitErrorHandler } from "react-hook-form";
import { ChangeEmailType, GetPersonalAccountType } from "../types/Account";
import { ChangeEmailSchema } from "../validation/schemas";
import { Typography, Divider, Button } from "@mui/material";
import FormComponent from "./FormComponent";
import TextFieldComponent from "./TextFieldComponent";
import { useManageAccounts } from "../hooks/useManageAccounts";
import { useEffect } from "react";

export default function ChangeAccountEmailComponent({ account, fetchAccount }: { account: GetPersonalAccountType | null, fetchAccount: () => void }) {
  const { updateAccountEmail } = useManageAccounts();
  const {
    handleSubmit,
    control,
    formState: { errors },
    trigger,
    setValue,
  } = useForm<ChangeEmailType>({
    defaultValues: {
      email: account?.email
    },
    resolver: yupResolver(ChangeEmailSchema),
  });

  const onSubmit: SubmitHandler<ChangeEmailType> = async (data) => {
    const err = await updateAccountEmail(account?.id ?? "", data);
    if (!err) {
      fetchAccount();
    }
  };

  const onError: SubmitErrorHandler<ChangeEmailType> = (error) => {
    console.error(error);
  };

  useEffect(() => {
    setValue("email", account?.email ?? "");
  }, [account]);

  return (
    <FormComponent
      handleSubmit={handleSubmit}
      onError={onError}
      onSubmit={onSubmit}
      align="start"
    >
      <Typography variant="h4">Change e-mail</Typography>
      <Typography variant="body1">Enter new e-mail address below!</Typography>
      <Divider
        sx={{
          marginTop: "1rem",
        }}
      ></Divider>
      <TextFieldComponent
        control={control}
        errors={errors}
        label="New e-mail"
        name="email"
        trigger={trigger}
        type="text"
      />
      <Button
        type="submit"
        variant="contained"
        sx={{
          marginY: 2
        }}
      >
        Save changes
      </Button>
    </FormComponent>
  )
}