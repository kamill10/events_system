import { yupResolver } from "@hookform/resolvers/yup";
import { useForm, SubmitHandler, SubmitErrorHandler } from "react-hook-form";
import { ChangeEmailType, GetPersonalAccountType } from "../types/Account";
import { ChangeEmailSchema } from "../validation/schemas";
import { Typography, Divider, Button } from "@mui/material";
import FormComponent from "./FormComponent";
import TextFieldComponent from "./TextFieldComponent";
import { useManageAccounts } from "../hooks/useManageAccounts";
import { useEffect, useState } from "react";
import ConfirmChangeModal from "./ConfirmChangeModal";
import { useTranslation } from "react-i18next";

export default function ChangeAccountEmailComponent({
  account,
  fetchAccount,
}: {
  account: GetPersonalAccountType | null;
  fetchAccount: () => void;
}) {
  const { t } = useTranslation();
  const { updateAccountEmail } = useManageAccounts();
  const [open, setOpen] = useState(false);
  const {
    handleSubmit,
    control,
    formState: { errors },
    trigger,
    setValue,
    getValues,
  } = useForm<ChangeEmailType>({
    defaultValues: {
      email: account?.email,
    },
    resolver: yupResolver(ChangeEmailSchema),
  });

  const handleRequest = async () => {
    const err = await updateAccountEmail(account?.id ?? "", getValues());
    if (!err) {
      fetchAccount();
    }
  };

  const onSubmit: SubmitHandler<ChangeEmailType> = async (_) => {
    setOpen(true);
  };

  const onError: SubmitErrorHandler<ChangeEmailType> = (error) => {
    console.error(error);
  };

  useEffect(() => {
    setValue("email", account?.email ?? "");
  }, [account]);

  return (
    <>
      <FormComponent
        handleSubmit={handleSubmit}
        onError={onError}
        onSubmit={onSubmit}
        align="start"
      >
        <Typography variant="h4">{t("changeEmail")}</Typography>
        <Typography variant="body1">{t("enterNewMail")}</Typography>
        <Divider
          sx={{
            marginTop: "1rem",
          }}
        ></Divider>
        <TextFieldComponent
          control={control}
          errors={errors}
          label={t("newEmail")}
          name="email"
          trigger={trigger}
          type="text"
        />
        <Button
          type="submit"
          variant="contained"
          sx={{
            marginY: 2,
          }}
        >
          {t("saveChanges")}
        </Button>
      </FormComponent>
      <ConfirmChangeModal
        callback={handleRequest}
        handleClose={() => setOpen(false)}
        open={open}
      ></ConfirmChangeModal>
    </>
  );
}
