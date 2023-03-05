
import com.google.gson.annotations.SerializedName

data class (
    @SerializedName("id")
    val id: String,
    @SerializedName("positions")
    val positions: List<Position>
)