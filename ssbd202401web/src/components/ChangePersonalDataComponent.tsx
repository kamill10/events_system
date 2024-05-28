import { Box, Button, MenuItem, TextField, Typography } from "@mui/material";
import { Controller, SubmitErrorHandler, SubmitHandler, useForm } from "react-hook-form";
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
      accountTimeZone: account?.accountTimeZone ?? "Europe/Warsaw"
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
    setValue("accountTimeZone", account?.accountTimeZone ?? "Europe/Warsaw");
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
        <Typography variant="h4">{t("changePersonalDataTitle")}</Typography>
        <Typography variant="body1">{t("changePersonalDataBody")}</Typography>
        <Button
            onClick={getMyAccount}
            variant="contained"
            startIcon={<RefreshIcon />}
            color="secondary"
            sx={{
              mt: 1,
              mb: 2,
              width: "fit-content",
            }}
          >
            {t("refreshData")}
          </Button>
        <TextFieldComponent
          control={control}
          errors={errors}
          label={t("firstName") + "*"}
          name="firstName"
          trigger={trigger}
          type="text"
        />
        <TextFieldComponent
          control={control}
          errors={errors}
          label={t("lastName") + "*"}
          name="lastName"
          trigger={trigger}
          type="text"
        />
        <GenderListComponent control={control} errors={errors} name="gender" />
        <Controller
          name="accountTimeZone"
          control={control}
          render={({ field }) => {
            return (
              <TextField
                select
                label={t("timeZone") + "*"}
                value={field.value}
                onChange={e => {
                  field.onChange(e);
                }}
                id={field.name}
                sx={{ marginTop: "1rem" }}
                name={field.name}
                autoComplete=""
              >
                <MenuItem value="Europe/Warsaw">
                  {t("europeWarsaw")}
                </MenuItem>
                <MenuItem value="Europe/London">
                  {t("europeLondon")}
                </MenuItem>
              </TextField>
            )
          }}
        ></Controller>
        <Box
          sx={{
            display: "flex",
            justifyContent: "flex-start",
            gap: "1rem",
            marginTop: "3rem",
          }}
        >
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
