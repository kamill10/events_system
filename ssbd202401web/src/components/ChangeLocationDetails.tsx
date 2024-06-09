import ChangeLocationDataComponent from "./ChangeLocationDataComponent.tsx";
import { Location } from "../types/Location.ts";
import DeleteLocationComponent from "./DeleteLocationComponent.tsx";

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
      />
      {location && (
        <DeleteLocationComponent
          locationId={location.id}
          fetchLocation={fetchLocation}
        />
      )}
    </>
  );
}
