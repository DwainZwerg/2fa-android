package app.ninesevennine.twofactorauthenticator.utils

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object Constants {
    const val NILUUIDSTR = "00000000-0000-0000-0000-000000000000"
    @OptIn(ExperimentalUuidApi::class)
    val NILUUID = Uuid.parse(NILUUIDSTR)

    const val ONEUUIDSTR = "00000000-0000-0000-0000-000000000001"
}