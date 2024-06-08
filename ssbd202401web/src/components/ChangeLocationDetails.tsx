import ChangeLocationDataComponent from "./ChangeLocationDataComponent.tsx";
import {Location} from "../types/Location.ts";

export default function ChangeLocationDetails({
  location,
  fetchLocation,
}: {
  location: Location | null;
  fetchLocation: () => void;
}) {
  return (
    <>
      <ChangeLocationDataComponent
        location={location}
        fetchLocation={fetchLocation}
      ></ChangeLocationDataComponent>
    </>
  );
}