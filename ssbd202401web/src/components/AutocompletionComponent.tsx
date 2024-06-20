import { Autocomplete, TextField, Typography } from "@mui/material";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useLocations } from "../hooks/useLocations";
import { useSpeakers } from "../hooks/useSpeakers";
import { RoomType } from "../types/Room";
import { AxiosError } from "axios";
import { Location } from "../types/Location";
import { Speaker } from "../types/Speaker";
import { FieldErrors, UseFormTrigger } from "react-hook-form";

export default function AutocompletionComponent({
  setSpeakerId,
  setRoomId,
  setLocationId,
  errors,
  trigger,
  update,
}: {
  setSpeakerId: (speakerId: string) => void;
  setRoomId: (roomId: string) => void;
  setLocationId: (locationId: string) => void;
  errors: FieldErrors;
  trigger: UseFormTrigger<any>;
  update?: boolean | null;
}) {
  const { t } = useTranslation();
  const { getAllLocations, getAllRoomsByLocationId } = useLocations();
  const { getAllSpeakers } = useSpeakers();
  const [locations, setLocations] = useState<Location[]>([]);
  const [rooms, setRooms] = useState<RoomType[] | null>(null);
  const [speakers, setSpeakers] = useState<Speaker[]>([]);

  async function fetchLocations() {
    const data = await getAllLocations();
    if (!(data instanceof AxiosError)) {
      setLocations(data as Location[]);
    }
  }

  async function fetchRooms(locationName: string | null) {
    const location = locations.filter((value) => value.name == locationName)[0];
    if (location) {
      setLocationId(location.id);
      const data = await getAllRoomsByLocationId(location.id);
      if (!(data instanceof AxiosError)) {
        setRooms(data as RoomType[]);
      }
    } else {
      setLocationId("");
    }
  }

  function setSpeaker(fullSpeaker: string | null) {
    const speaker = speakers.filter(
      (obj) => `${obj.firstName} ${obj.lastName}` == fullSpeaker,
    )[0];
    if (speaker) {
      setSpeakerId(speaker.id);
    } else {
      setSpeakerId("");
    }
  }

  function setRoom(roomName: string | null) {
    const room = rooms?.filter((obj) => obj.name == roomName)[0];
    if (room) {
      setRoomId(room.id);
    } else {
      setRoomId("");
    }
  }

  async function fetchSpeakers() {
    const data = await getAllSpeakers();
    if (!(data instanceof AxiosError)) {
      setSpeakers(data as Speaker[]);
    }
  }

  useEffect(() => {
    fetchLocations().then(fetchSpeakers);
  }, []);

  return (
    <>
      <Autocomplete
        sx={{
          marginTop: "1rem",
        }}
        disablePortal
        fullWidth
        onChange={(_, value) => {
          setSpeaker(value);
          setTimeout(() => trigger("speakerId"), 500);
        }}
        options={speakers.map(
          (speaker) => `${speaker.firstName} ${speaker.lastName}`,
        )}
        renderInput={(params) => {
          return (
            <TextField
              placeholder={update ? t("noChange") : ""}
              label={t("speakerIdLabel") + "*"}
              error={errors["speakerId"] ? true : false}
              {...params}
            ></TextField>
          );
        }}
      ></Autocomplete>
      <Typography color={"red"} fontSize={14} width={"inherit"} margin={"none"}>
        {errors["speakerId"]?.message as string}
      </Typography>
      <Autocomplete
        sx={{
          marginTop: "1rem",
        }}
        disablePortal
        fullWidth
        onChange={(_, value) => {
          setRooms(null);
          fetchRooms(value);
          setTimeout(() => trigger("locationId"), 500);
        }}
        options={locations.map((location) => location.name)}
        renderInput={(params) => {
          return (
            <TextField
              placeholder={update ? t("noChange") : ""}
              label={t("locationIdLabel") + "*"}
              error={errors["locationId"] ? true : false}
              {...params}
            ></TextField>
          );
        }}
      ></Autocomplete>
      <Typography color={"red"} fontSize={14} width={"inherit"} margin={"none"}>
        {errors["locationId"]?.message as string}
      </Typography>
      <Autocomplete
        sx={{
          marginTop: "1rem",
        }}
        disablePortal
        color="red"
        fullWidth
        disabled={!rooms}
        onChange={(_, value) => {
          setRoom(value);
          setTimeout(() => trigger("roomId"), 500);
        }}
        options={rooms?.map((room) => room.name) ?? []}
        renderInput={(params) => {
          return (
            <TextField
              placeholder={update ? t("noChange") : ""}
              label={t("roomIdLabel") + "*"}
              error={errors["roomId"] ? true : false}
              {...params}
            ></TextField>
          );
        }}
      ></Autocomplete>
      <Typography color={"red"} fontSize={14} width={"inherit"} margin={"none"}>
        {errors["roomId"]?.message as string}
      </Typography>
    </>
  );
}
