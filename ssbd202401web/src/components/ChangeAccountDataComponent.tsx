import { yupResolver } from "@hookform/resolvers/yup";
import { useForm, SubmitHandler, SubmitErrorHandler } from "react-hook-form";
import { GetPersonalAccountType, UpdatePersonalDataType } from "../types/Account";
import { changePersonalDataSchema } from "../validation/schemas";
import { Typography, Divider, Button } from "@mui/material";
import FormComponent from "./FormComponent";
import GenderListComponent from "./GenderListComponent";
import TextFieldComponent from "./TextFieldComponent";
import { useManageAccounts } from "../hooks/useManageAccounts";
import { useEffect } from "react";

export default function ChangeAccountDataComponent({ account, fetchAccount }: { account: GetPersonalAccountType | null, fetchAccount: () => void }) {
  const { updateAccountData } = useManageAccounts();
  const {
    handleSubmit,
    control,
    formState: { errors },
    trigger,
    setValue,
  } = useForm<UpdatePersonalDataType>({
    defaultValues: {
      firstName: account?.firstName,
      lastName: account?.lastName,
      gender: account?.gender
    },
    resolver: yupResolver(changePersonalDataSchema),
  });

  const onSubmit: SubmitHandler<UpdatePersonalDataType> = async (data) => {
    const err = await updateAccountData(account?.id ?? "", data);
    if (!err) {
      fetchAccount();
    }
  };

  const onError: SubmitErrorHandler<UpdatePersonalDataType> = (error) => {
    console.error(error);
  };

  useEffect(() => {
    setValue("firstName", account?.firstName ?? "");
    setValue("lastName", account?.lastName ?? "");
    setValue("gender", account?.gender ?? 0);
  }, [account])

  return (
    <FormComponent
      handleSubmit={handleSubmit}
      onError={onError}
      onSubmit={onSubmit}
      align="start"
    >
      <Typography variant="h4">Change personal data</Typography>
      <Typography variant="body1">Enter account's new personal data below!</Typography>
      <Divider
        sx={{
          marginTop: "1rem",
        }}
      ></Divider>
      <TextFieldComponent
        control={control}
        errors={errors}
        label="First name"
        name="firstName"
        trigger={trigger}
        type="text"
      />
      <TextFieldComponent
        control={control}
        errors={errors}
        label="Last name"
        name="lastName"
        trigger={trigger}
        type="text"
      />
      <GenderListComponent
        control={control}
        errors={errors}
        name="gender"
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