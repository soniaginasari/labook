package com.example.labook2.apihelper;

import com.example.labook2.model.ResponseBerita;
import com.example.labook2.model.ResponseBidang;
import com.example.labook2.model.ResponseLaboran;
import com.example.labook2.model.ResponseLaboratorium;
import com.example.labook2.model.ResponseLayanan;
import com.example.labook2.model.ResponseNotifikasi;
import com.example.labook2.model.ResponseSewa;
import com.example.labook2.model.ResponseUser;
import com.example.labook2.model.ResponseVerifLaboran;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BaseApiService {
    @GET("laboratorium")
    Call<ResponseLaboratorium> getLaboratorium();

    @FormUrlEncoded
    @POST("createlaboraotirum")
    Call<ResponseBody> SetLaboratorium( @Field("nama_lab") String nama_lab,
                                        @Field("no_telp") String no_telp,
                                        @Field("alamat") String alamat,
                                  @Field("foto_lab") String foto_lab);

    @FormUrlEncoded
    @POST("editlaboratorium")
    Call<ResponseBody> EditLaboratorium(@Field("id_laboratorium") String id_laboratorium,
                                        @Field("nama_lab") String nama_lab,
                                        @Field("no_telp") String no_telp,
                                        @Field("alamat") String alamat,
                                        @Field("foto_lab") String foto_lab,
                                        @Field("id_verif_lab") String id_verif_lab
    );

    @FormUrlEncoded
    @POST("hapuslaboratorium")
    Call<ResponseBody> HapusLaboratorium(@Field("id_laboratorium") String id_laboratorium
    );

    @FormUrlEncoded
    @POST("createveriflab")
    Call<ResponseBody> SetVerifLab(@Field("id_laboratorium") String id_laboratorium,
                                        @Field("nama_lab") String nama_lab,
                                        @Field("no_telp") String no_telp,
                                        @Field("alamat") String alamat,
                                        @Field("foto_lab") String foto_lab,
                                        @Field("ket_crud") String ket_crud);

    @FormUrlEncoded
    @POST("createverifbidang")
    Call<ResponseBody> SetVerifBidang(@Field("id_laboratorium") String id_laboratorium,
                                      @Field("id_bidang") String id_bidang,
                                      @Field("nama_bidang") String nama_bidang,
                                      @Field("ket_crud") String ket_crud);

    @FormUrlEncoded
    @POST("createveriflayanan")
    Call<ResponseBody> SetVerifLayanan(@Field("id_laboratorium") String id_laboratorium,
                                       @Field("id_layanan") String id_layanan,
                                       @Field("nama_layanan") String nama_layanan,
                                       @Field("unit_satuan") String unit_satuan,
                                       @Field("satuan") String satuan,
                                       @Field("harga") String harga,
                                       @Field("id_bidang") String id_bidang,
                                       @Field("keterangan") String keterangan,
                                   @Field("ket_crud") String ket_crud);

    @GET("bidang")
    Call<ResponseBidang> getBidang(@Query("id_laboratorium") String id_laboratorium);

    @FormUrlEncoded
    @POST("createbidang")
    Call<ResponseBody> SetBidang( @Field("nama_bidang") String nama_bidang,
                                @Field("id_laboratorium") String id_laboratorium,
                                  @Field("id_verif_bidang") String id_verif_bidang);

    @FormUrlEncoded
    @POST("editbidang")
    Call<ResponseBody> EditBidang(@Field("id_bidang") String id_bidang,
                                      @Field("nama_bidang") String nama_bidang,
                                  @Field("id_verif_bidang") String id_verif_bidang
    );

    @FormUrlEncoded
    @POST("hapusbidang")
    Call<ResponseBody> HapusBidang(@Field("id_bidang") String id_bidang,
                                   @Field("id_verif_bidang") String id_verif_bidang
    );

    @GET("layanan")
    Call<ResponseLayanan> getLayanan(@Query("id_bidang") String id_bidang);

    @FormUrlEncoded
    @POST("createlayanan")
    Call<ResponseBody> SetLayanan( @Field("nama_layanan") String nama_layanan,
                                   @Field("unit_satuan") String unit_satuan,
                                   @Field("satuan") String satuan,
                                   @Field("harga") String harga,
                                   @Field("id_bidang") String id_bidang,
                                  @Field("keterangan") String keterangan,
                                   @Field("id_verif_layanan") String id_verif_layanan);

    @FormUrlEncoded
    @POST("editlayanan")
    Call<ResponseBody> EditLayanan(@Field("id_layanan") String id_layanan,
                                   @Field("nama_layanan") String nama_layanan,
                                   @Field("unit_satuan") String unit_satuan,
                                   @Field("satuan") String satuan,
                                   @Field("harga") String harga,
                                   @Field("id_bidang") String id_bidang,
                                   @Field("keterangan") String keterangan,
                                   @Field("id_verif_layanan") String id_verif_layanan
    );

    @FormUrlEncoded
    @POST("hapuslayanan")
    Call<ResponseBody> HapusLayanan(@Field("id_layanan") String id_layanan,
                                    @Field("id_verif_layanan") String id_verif_layanan
    );

    @GET("getveriflab")
    Call<ResponseVerifLaboran> getVerifLab(@Query("id_laboratorium") String id_laboratorium);

    @GET("getverifbidang")
    Call<ResponseVerifLaboran> getVerifBidang(@Query("id_laboratorium") String id_laboratorium);

    @GET("getveriflayanan")
    Call<ResponseVerifLaboran> getVerifLayanan(@Query("id_laboratorium") String id_laboratorium);

    @GET("search_lab")
    Call<ResponseLaboratorium> getSearchLab(@Query("layanan") String layanan);

    @GET("sewa")
    Call<ResponseSewa> getSewa(@Query("id_peminjam") String id_peminjam,
                               @Query("keterangan") String keterangan);

    @GET("sewa_history")
    Call<ResponseSewa> getSewaHistory(@Query("id_peminjam") String id_peminjam,
                                      @Query("ket_pengerjaan") String ket_pengerjaan);

    @GET("sewa_laboran")
    Call<ResponseSewa> getSewaLaboran(@Query("id_laboran") String id_laboran,
                                      @Query("keterangan") String keterangan);

    @GET("sewa_laboran_history")
    Call<ResponseSewa> getSewaLaboranHistory(@Query("id_laboran") String id_laboran,
                                             @Query("ket_pengerjaan") String ket_pengerjaan);

    @GET("laboran")
    Call<ResponseLaboran> getLaboran(@Query("id_laboratorium") String id_laboratorium);

    @FormUrlEncoded
    @POST("createlaboran")
    Call<ResponseBody> registerLaboran(@Field("name") String name,
                                       @Field("alamat") String alamat,
                                       @Field("no_hp") String no_hp,
                                       @Field("email") String email,
                                       @Field("hak_akses") String hak_akses,
                                       @Field("password") String password,
                                       @Field("c_password") String c_password,
                                       @Field("id_laboratorium") String id_laboratorium);

    @FormUrlEncoded
    @POST("editlaboran")
    Call<ResponseBody> editLaboran(@Field("id_laboran") String id_laboran,
                                       @Field("hak_akses") String hak_akses
                                       );

    @FormUrlEncoded
    @POST("hapuslaboran")
    Call<ResponseBody> HapusLaboran(@Field("id_laboran") String id_laboran
    );

    @GET("lab_laboran")
    Call<ResponseLaboran> getLabLaboran(@Query("id_laboran") String id_laboran);

    @FormUrlEncoded
    @POST("id_laboratorium")
    Call<ResponseBody> getId_laboratorium(@Field("id_laboran") String id_laboran
    );

    @FormUrlEncoded
    @POST("akses_laboran")
    Call<ResponseBody> getAkses_laboran(@Field("id_laboran") String id_laboran
    );

    @GET("getuser")
    Call<ResponseUser> getUserLaboran();

    @FormUrlEncoded
    @POST("get_user")
    Call<ResponseBody> getUser(@Field("id_laboran") String id_laboran);

    @FormUrlEncoded
    @POST("cal_sewa")
    Call<ResponseBody> getCal_sewa(@Field("id_laboran") String id_laboran
    );

    @GET("cal_det_sewa")
    Call<ResponseSewa> getCalDetSewa(@Query("id_laboran") String id_laboran,
                                        @Query("tgl_pinjam") String tgl_pinjam);

    @FormUrlEncoded
    @POST("get_chart")
    Call<ResponseBody> getChart(@Field("id_laboran") String id_laboran,
                                @Field("tahun") String tahun
    );

    @GET("berita")
    Call<ResponseBerita> getBerita();

    @GET("beritalaboran")
    Call<ResponseBerita> getBeritaLaboran(@Query("id_laboratorium") String id_laboratorium);

    @FormUrlEncoded
    @POST("createberita")
    Call<ResponseBody> SetBerita( @Field("judul") String judul,
                                   @Field("isi") String isi,
                                   @Field("id_laboratorium") String id_laboratorium
                                  );

    @FormUrlEncoded
    @POST("editberita")
    Call<ResponseBody> EditBerita(@Field("id_berita") String id_berita,
                                  @Field("judul") String judul,
                                  @Field("isi") String isi,
                                  @Field("id_laboratorium") String id_laboratorium
    );

    @FormUrlEncoded
    @POST("hapusberita")
    Call<ResponseBody> HapusBerita(@Field("id_berita") String id_berita
    );

    @Multipart
    @POST("tambahsewa")
    Call<ResponseBody> SetSewa( @Part("tgl_pinjam") RequestBody tgl_pinjam,
                                @Part("jumlah") RequestBody jumlah,
                                @Part("satuan") RequestBody satuan,
                                @Part("harga") RequestBody harga,
                                @Part("total_harga") RequestBody total_harga,
                                @Part MultipartBody.Part pdf,
                                @Part("name") RequestBody name,
                                @Part("keterangan") RequestBody keterangan,
                                @Part("id_peminjam") RequestBody id_peminjam,
                                @Part("id_laboratorium") RequestBody id_laboratorium,
                                @Part("id_layanan") RequestBody id_layanan);

    @FormUrlEncoded
    @POST("editstatus")
    Call<ResponseBody> EditKeterangan(@Field("id_peminjaman") String id_peminjaman,
                                  @Field("keterangan") String keterangan,
                                  @Field("alasan") String alasan,
                                  @Field("ket_pembayaran") String ket_pembayaran,
                                  @Field("id_peminjam") String id_peminjam
    );

    @FormUrlEncoded
    @POST("editpengerjaan")
    Call<ResponseBody> EditPengerjaan(@Field("id_peminjaman") String id_peminjaman,
                                      @Field("ket_pengerjaan") String ket_pengerjaan,
                                      @Field("id_peminjam") String id_peminjam
    );

    @Multipart
    @POST("pdf_selesai")
    Call<ResponseBody> SetPdfSelesai(
                                @Part("id_peminjaman") RequestBody id_peminjaman,
                                @Part MultipartBody.Part pdf,
                                @Part("id_peminjam") RequestBody id_peminjam);

    @FormUrlEncoded
    @POST("editvalbayar")
    Call<ResponseBody> EditValBayar(@Field("id_peminjaman") String id_peminjaman,
                                    @Field("id_peminjam") String id_peminjam
                                        );

    @FormUrlEncoded
    @POST("tgl_tenggat")
    Call<ResponseBody> getTgl_tenggat(@Field("id_peminjaman") String id_peminjaman
    );

    @FormUrlEncoded
    @POST("tgl_bayar")
    Call<ResponseBody> getTgl_bayar(@Field("id_peminjaman") String id_peminjaman
    );

    @FormUrlEncoded
    @POST("editmetpembayaran")
    Call<ResponseBody> EditMetPembayaran(@Field("id_peminjaman") String id_peminjaman,
                                      @Field("metode_pembayaran") String metode_pembayaran
    );

    @FormUrlEncoded
    @POST("edittglbayar")
    Call<ResponseBody> EditTglBayar(@Field("id_peminjaman") String id_peminjaman,
                                    @Field("tgl_bayar") String tgl_bayar,
                                    @Field("id_laboratorium") String id_laboratorium
    );

    @FormUrlEncoded
    @POST("editbukti")
    Call<ResponseBody> EditBukti(@Field("id_peminjaman") String id_peminjaman,
                                 @Field("bukti_pembayaran") String bukti_pembayaran,
                                 @Field("id_laboratorium") String id_laboratorium
    );

    @Multipart
    @POST("editsewa")
    Call<ResponseBody> EditSewa(@Part("id_peminjaman") RequestBody id_peminjaman,
                                @Part("tgl_pinjam") RequestBody tgl_pinjam,
                                @Part MultipartBody.Part pdf,
                                @Part("jumlah") RequestBody jumlah,
                                @Part("total_harga") RequestBody total_harga,
                                @Part("id_laboratorium") RequestBody id_laboratorium
    );

    @FormUrlEncoded
    @POST("hapussewa")
    Call<ResponseBody> HapusSewa(@Field("id_peminjaman") String id_peminjaman
    );

    @POST("details")
    Call<ResponseBody> authRequest(@Header("Authorization") String Authorization);


    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> loginRequest(@Field("email") String email,
                                    @Field("password") String password,
                                    @Field("token") String token);

    @FormUrlEncoded
    @POST("register")
    Call<ResponseBody> registerRequest(@Field("name") String name,
                                       @Field("alamat") String alamat,
                                       @Field("no_hp") String no_hp,
                                       @Field("email") String email,
                                       @Field("hak_akses") String hak_akses,
                                       @Field("password") String password,
                                       @Field("c_password") String c_password);

    @FormUrlEncoded
    @POST("editprofile")
    Call<ResponseBody> editRequest( @Field("id") String id,
                                    @Field("name") String name,
                                    @Field("no_hp") String no_hp,
                                    @Field("email") String email,
                                    @Field ("alamat") String alamat,
                                    @Field ("foto_user") String foto_user);

    @FormUrlEncoded
    @POST("insert_token")
    Call<ResponseBody> insertToken(@Field("fcm_token") String fcm_token,
                                   @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("delete_token")
    Call<ResponseBody> deleteToken(@Field("fcm_token") String fcm_token,
                                   @Field("user_id") String user_id);

    @GET("get_notifikasi")
    Call<ResponseNotifikasi> getNotifikasi(@Query("id_user") String id_user);



}
