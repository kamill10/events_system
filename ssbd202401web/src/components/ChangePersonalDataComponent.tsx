import {
  Box,
  Button,
  Divider,
  MenuItem,
  TextField,
  Typography,
} from "@mui/material";
import {
  Controller,
  SubmitErrorHandler,
  SubmitHandler,
  useForm,
} from "react-hook-form";
import RefreshIcon from "@mui/icons-material/Refresh";
import SaveIcon from "@mui/icons-material/Save";
import { PersonalDataType } from "../types/PersonalData";
import { useAccount } from "../hooks/useAccount";
import { GenderEnum } from "../types/enums/Gender.enum";
import { useEffect } from "react";
import { yupResolver } from "@hookform/resolvers/yup";
import { changePersonalDataSchema } from "../validation/schemas";
import FormComponent from "./FormComponent";
import TextFieldComponent from "./TextFieldComponent";

export default function ChangePersonalDataComponent() {
  const { account, updateMyPersonalData, getMyAccount } = useAccount();
  const {
    handleSubmit,
    control,
    formState: { errors },
    trigger,
    setValue,
  } = useForm<PersonalDataType>({
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
  }, [account, setValue]);

  const onSubmit: SubmitHandler<PersonalDataType> = async (data) => {
    updateMyPersonalData(data);
  };

  const onError: SubmitErrorHandler<PersonalDataType> = (error) => {
    console.error(error);
  };

  return (
    <FormComponent
      handleSubmit={handleSubmit}
      onError={onError}
      onSubmit={onSubmit}
      align="start"
    >
      <Typography variant="h4">Change personal data</Typography>
      <Divider
        sx={{
          marginTop: "3rem",
          marginBottom: "3rem",
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
      <Controller
        name="gender"
        control={control}
        render={({ field }) => {
          return (
            <TextField
              select
              label="Gender"
              value={field.value}
              onChange={(e) => {
                field.onChange(e);
                setTimeout(
                  () => trigger(e.target.name as keyof PersonalDataType),
                  500,
                );
              }}
              id={field.name}
              sx={{ marginTop: "1rem" }}
              name={field.name}
              autoComplete=""
            >
              {Object.keys(GenderEnum).map((key, value) => {
                return (
                  <MenuItem key={key} value={value}>
                    {GenderEnum[key as keyof typeof GenderEnum].info}
                  </MenuItem>
                );
              })}
            </TextField>
          );
        }}
      ></Controller>
      <Typography
        color={"red"}
        fontSize={14}
        width={"inherit"}
        sx={{
          marginBottom: "1rem",
        }}
      >
        {errors.gender?.message}
      </Typography>
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
          Refresh Data
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
          Save changes
        </Button>
      </Box>
    </FormComponent>
  );
}
