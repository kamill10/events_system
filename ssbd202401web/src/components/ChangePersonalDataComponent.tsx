import { Box, Button, Divider, Typography } from "@mui/material";
import { SubmitErrorHandler, SubmitHandler, useForm } from "react-hook-form";
import RefreshIcon from "@mui/icons-material/Refresh";
import SaveIcon from "@mui/icons-material/Save";
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';

import { useAccount } from "../hooks/useAccount";
import { useEffect, useState } from "react";
import { yupResolver } from "@hookform/resolvers/yup";
import { changePersonalDataSchema } from "../validation/schemas";
import FormComponent from "./FormComponent";
import TextFieldComponent from "./TextFieldComponent";
import GenderListComponent from "./GenderListComponent";
import { UpdatePersonalDataType } from "../types/Account";

export default function ChangePersonalDataComponent() {
  const { account, updateMyPersonalData, getMyAccount } = useAccount();
  const [open, setOpen] = useState(false);
  const [formData, setFormData] = useState<UpdatePersonalDataType | null>(null);


  const {
    handleSubmit,
    control,
    formState: { errors },
    trigger,
    setValue,
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
  }, [account, setValue]);

  const onSubmit: SubmitHandler<UpdatePersonalDataType> = (data) => {
    setFormData(data);
    setOpen(true);
  };

  const onError: SubmitErrorHandler<UpdatePersonalDataType> = (error) => {
    console.error(error);
  };

  const handleSendData = () => {
    if (formData) {
      updateMyPersonalData(formData);
      setOpen(false);
    }
  };

  const handleDontSendData = () => {
    setOpen(false);
  };

  return (
    <>
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
    <Dialog
        open={open}
        onClose={handleDontSendData}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <DialogTitle id="alert-dialog-title">{"Updating Data"}</DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description">
            Are you sure you want to change data?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleSendData}>Change</Button>
          <Button onClick={handleDontSendData} autoFocus>
            Don't
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
}
