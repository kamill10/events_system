import { TableCell, TableRow} from "@mui/material";
import {GetRoomResponse, RoomType} from "../types/Room.ts";
import ChangeRoomDetailsComponent from "./ChangeRoomDetailsComponent.tsx";
import {useEffect, useState} from "react";
import useNotification from "../hooks/useNotification.tsx";
import {useLocations} from "../hooks/useLocations.ts";
import {useTranslation} from "react-i18next";
import ModalComponent from "./ModalComponent.tsx";

export default function RoomRowComponent({ room }: { room: RoomType }) {
    const [open, setOpen] = useState(false);
    const [detailedRoom,setRoom] = useState<GetRoomResponse | null>(null);
    const sendNotification = useNotification();
    const {getRoomById} = useLocations();
    const {t} = useTranslation();
    const handleClick = () => {
        setOpen(true);
    };
    async function fetchRoom() {
        if (room.id) {
            getRoomById(room.id).then((value) => {
                setRoom(value as GetRoomResponse);
            });
        } else {
            sendNotification({
                type: "error",
                description: t("getRoomByFail"),
            });
        }
    }

    useEffect(() => {
        fetchRoom();
    }, []);
    return (
        <>
            <TableRow hover onClick={handleClick}>
                <TableCell>{room.id}</TableCell>
                <TableCell align="right">{room.name}</TableCell>
                <TableCell align="right">{room.maxCapacity}</TableCell>
            </TableRow>
            <ModalComponent open={open} onClose={()=>setOpen(false)}  width={400}>
                <ChangeRoomDetailsComponent room={detailedRoom} fetchRoom={fetchRoom} setModal={ () =>setOpen(false)} />
            </ModalComponent>
        </>
    );
}
