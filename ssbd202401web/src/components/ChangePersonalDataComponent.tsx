import { Box, Button, Divider, Typography } from "@mui/material";
import { SubmitErrorHandler, SubmitHandler, useForm } from "react-hook-form";
import RefreshIcon from "@mui/icons-material/Refresh";
import SaveIcon from "@mui/icons-material/Save";
import { useAccount } from "../hooks/useAccount";
import { useEffect, useState } from "react";
import { yupResolver } from "@hookform/resolvers/yup";
import { changePersonalDataSchema } from "../validation/schemas";
import FormComponent from "./FormComponent";
import TextFieldComponent from "./TextFieldComponent";
import GenderListComponent from "./GenderListComponent";
import { UpdatePersonalDataType } from "../types/Account";
import ConfirmChangeModal from "./ConfirmChangeModal";
import { useTranslation } from "react-i18next";

export default function ChangePersonalDataComponent() {
  const { account, updateMyPersonalData, getMyAccount } = useAccount();
  const [open, setOpen] = useState(false);
  const { t } = useTranslation();

  const {
    handleSubmit,
    control,
    formState: { errors },
    trigger,
    setValue,
    getValues,
  } = useForm<UpdatePersonalDataType>({
    defaultValues: {
      firstName: account ? account.firstName : "",
      lastName: account ? account.lastName : "",
      gender: account ? account.gender : 0,
    },
    resolver: yupResolver(changePersonalDataSchema),
  });

  useEffect(() => {
    getMyAccount();
  }, []);

  useEffect(() => {
    setValue("firstName", account?.firstName ?? "");
    setValue("lastName", account?.lastName ?? "");
    setValue("gender", account?.gender ?? 0);
    trigger();
  }, [account, setValue]);

  const onSubmit: SubmitHandler<UpdatePersonalDataType> = () => {
    setOpen(true);
  };

  const onError: SubmitErrorHandler<UpdatePersonalDataType> = (error) => {
    console.error(error);
  };

  return (
    <>
      <FormComponent
        handleSubmit={handleSubmit}
        onError={onError}
        onSubmit={onSubmit}
        align="start"
      >
        <Typography variant="h4">{t("changePersonalData")}</Typography>
        <Divider
          sx={{
            marginTop: "3rem",
            marginBottom: "3rem",
          }}
        ></Divider>
        <TextFieldComponent
          control={control}
          errors={errors}
          label={t("firstName")}
          name="firstName"
          trigger={trigger}
          type="text"
        />
        <TextFieldComponent
          control={control}
          errors={errors}
          label={t("lastName")}
          name="lastName"
          trigger={trigger}
          type="text"
        />
        <GenderListComponent control={control} errors={errors} name="gender" />
        <Box
          sx={{
            display: "flex",
            justifyContent: "flex-start",
            gap: "1rem",
            marginTop: "3rem",
          }}
        >
          <Button
            onClick={getMyAccount}
            variant="contained"
            startIcon={<RefreshIcon />}
            color="secondary"
            sx={{
              mt: 1,
              mb: 2,
              width: "fit-content",
              alignSelf: "center",
            }}
          >
            {t("refreshData")}
          </Button>
          <Button
            type="submit"
            variant="contained"
            startIcon={<SaveIcon />}
            sx={{
              mt: 1,
              mb: 2,
              width: "fit-content",
              alignSelf: "center",
            }}
          >
            {t("saveChanges")}
          </Button>
        </Box>
      </FormComponent>
      <ConfirmChangeModal
        open={open}
        handleClose={() => setOpen(false)}
        callback={() => {
          updateMyPersonalData(getValues());
        }}
      ></ConfirmChangeModal>
    </>
  );
}
