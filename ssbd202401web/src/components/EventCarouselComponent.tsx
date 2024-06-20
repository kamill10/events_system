import Card from "@mui/material/Card";
import CardActions from "@mui/material/CardActions";
import CardContent from "@mui/material/CardContent";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";

import { Event } from "../types/Event";
import { useNavigate } from "react-router-dom";
import { Paper } from "@mui/material";

interface Props {
  event: Event;
}

export default function EventCarouselComponent(props: Props) {
  const navigate = useNavigate();

  return (
    <Card
      sx={{
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
        height: "100%",
      }}
      component={Paper}
      elevation={1}
    >
      <CardContent>
        <Typography
          gutterBottom
          textAlign={"center"}
          variant="h5"
          component="div"
        >
          {props.event.name}
        </Typography>
        <Typography variant="body2" textAlign={"center"} color="text.secondary">
          {props.event.description}
        </Typography>
      </CardContent>
      <CardActions>
        <Button
          size="small"
          onClick={() => navigate(`/events/${props.event?.id}`)}
        >
          Learn More
        </Button>
      </CardActions>
    </Card>
  );
}
